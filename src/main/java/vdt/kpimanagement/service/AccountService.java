package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.AccountRequest;
import vdt.kpimanagement.dto.AccountResponse;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.AccountMapper;
import vdt.kpimanagement.repository.AccountRepo;
import vdt.kpimanagement.repository.AccountRoleRepo;
import vdt.kpimanagement.repository.EmployeeRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService extends BaseService<Account, AccountRequest, AccountResponse, Long> {

    private final AccountRepo accountRepo;
    private final AccountRoleRepo accountRoleRepo;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepo accountRepo,
                          AccountMapper accountMapper,
                          AccountRoleRepo accountRoleRepo,
                          EmployeeRepo employeeRepo,
                          PasswordEncoder passwordEncoder) {
        super(accountRepo, accountMapper);
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.accountRoleRepo = accountRoleRepo;
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Override getAll để enrich roles sau khi map
     */
    @Override
    public Page<AccountResponse> getAll(Pageable pageable) {
        return accountRepo.findByIsDeletedFalse(pageable)
                .map(account -> enrichWithRoles(accountMapper.toDto(account), account.getId()));
    }

    /**
     * Override search để enrich roles và thực hiện tìm kiếm qua repo
     */
    @Override
    public Page<AccountResponse> search(String keyword, Pageable pageable) {
        return accountRepo.searchByKeyword(keyword, pageable)
                .map(account -> enrichWithRoles(accountMapper.toDto(account), account.getId()));
    }

    /**
     * Override getById để enrich roles
     */
    @Override
    public AccountResponse getById(Long id) {
        Account account = accountRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản với ID: " + id));
        return enrichWithRoles(accountMapper.toDto(account), account.getId());
    }

    /**
     * Tạo tài khoản mới: kiểm tra username trùng, hash password, liên kết employee
     */
    @Override
    public AccountResponse create(AccountRequest request) {
        if (accountRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username đã tồn tại: " + request.getUsername());
        }
        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmployee(resolveEmployee(request.getEmployeeId()));
        account.setDeleted(false);
        Account saved = accountRepo.save(account);
        return enrichWithRoles(accountMapper.toDto(saved), saved.getId());
    }

    /**
     * Cập nhật tài khoản: chỉ cho phép thay đổi status, provider và employee
     */
    @Override
    public AccountResponse update(Long id, AccountRequest request) {
        Account account = accountRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản với ID: " + id));
        accountMapper.updateEntityFromDto(request, account);
        if (request.getEmployeeId() != null) {
            account.setEmployee(resolveEmployee(request.getEmployeeId()));
        }
        // Nếu gửi kèm password mới thì hash lại
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Account saved = accountRepo.save(account);
        return enrichWithRoles(accountMapper.toDto(saved), saved.getId());
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    private AccountResponse enrichWithRoles(AccountResponse response, Long accountId) {
        List<String> roles = accountRoleRepo.findByAccount_Id(accountId)
                .stream()
                .map(ar -> ar.getRole().getCode().name())
                .collect(Collectors.toList());
        response.setRoles(roles);
        return response;
    }

    private Employee resolveEmployee(Long employeeId) {
        if (employeeId == null) return null;
        return employeeRepo.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));
    }
}
