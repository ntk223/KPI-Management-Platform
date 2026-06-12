# AGENTS.md

## Bá»‘i cáº£nh dá»± Ã¡n
- ÄÃ¢y lÃ  backend Java Maven dÃ¹ng Spring Boot.
- Kiáº¿n trÃºc Æ°u tiÃªn: `Controller -> Service -> Repository`.
- Æ¯u tiÃªn sá»­a tá»‘i thiá»ƒu, khÃ´ng refactor lan rá»™ng náº¿u khÃ´ng cáº§n.
- Giá»¯ nguyÃªn style, cáº¥u trÃºc, naming convention vÃ  pattern hiá»‡n cÃ³ cá»§a project.

## Quy táº¯c code chung
- LuÃ´n Ä‘á»c code liÃªn quan trÆ°á»›c khi sá»­a: controller, service, repository, DTO, mapper, validation.
- Pháº£i fix root cause, khÃ´ng vÃ¡ táº¡m náº¿u cÃ³ thá»ƒ.
- KhÃ´ng Ä‘á»•i tÃªn class, method, package, API contract náº¿u khÃ´ng tháº­t sá»± cáº§n.
- KhÃ´ng thÃªm dependency má»›i náº¿u chÆ°a kiá»ƒm tra `pom.xml` vÃ  khÃ´ng cáº§n thiáº¿t cho task.
- KhÃ´ng sá»­a cÃ¡c pháº§n khÃ´ng liÃªn quan Ä‘áº¿n yÃªu cáº§u.
- Æ¯u tiÃªn reuse code hiá»‡n cÃ³, khÃ´ng duplicate logic.

## Quy táº¯c theo táº§ng Spring
- `Controller` chá»‰ xá»­ lÃ½ request, response vÃ  delegate xuá»‘ng `Service`.
- Business logic Ä‘áº·t á»Ÿ `Service`.
- `Repository` chá»‰ dÃ¹ng cho truy váº¥n vÃ  thao tÃ¡c DB.
- KhÃ´ng viáº¿t SQL hoáº·c business logic lá»›n trong `Controller`.
- Náº¿u má»™t luá»“ng xá»­ lÃ½ tÃ¡c Ä‘á»™ng nhiá»u báº£ng liÃªn quan, pháº£i cÃ¢n nháº¯c `@Transactional`.

## Quy táº¯c API vÃ  xá»­ lÃ½ lá»—i
- Giá»¯ Ä‘Ãºng response contract hiá»‡n cÃ³ cá»§a project. Náº¿u Ä‘Ã£ cÃ³ response wrapper hoáº·c global handler thÃ¬ pháº£i bÃ¡m theo pattern Ä‘Ã³.
- API pháº£i tráº£ vá» Ä‘Ãºng báº£n cháº¥t lá»—i Ä‘á»ƒ frontend dá»… kiá»ƒm soÃ¡t vÃ  xá»­ lÃ½.
- Æ¯u tiÃªn phÃ¢n biá»‡t rÃµ cÃ¡c nhÃ³m lá»—i nhÆ° `400`, `401`, `403`, `404`, `409`, `422`, `500` thay vÃ¬ tráº£ lá»—i mÆ¡ há»“.
- Business error pháº£i cÃ³ thÃ´ng Ä‘iá»‡p rÃµ rÃ ng, nháº¥t quÃ¡n, khÃ´ng lÃ m lá»™ chi tiáº¿t ná»™i bá»™ khÃ´ng cáº§n thiáº¿t.
- KhÃ´ng catch exception quÃ¡ chung chung rá»“i tráº£ vá» thÃ nh cÃ´ng giáº£ hoáº·c tráº£ sai mÃ£ lá»—i.
- KhÃ´ng tráº£ stack trace ra API.
- Giá»¯ nguyÃªn pattern xá»­ lÃ½ lá»—i hiá»‡n táº¡i cá»§a project náº¿u Ä‘Ã£ cÃ³.

## Quy táº¯c log vÃ  thÃ´ng Ä‘iá»‡p lá»—i
- Æ¯u tiÃªn viáº¿t `log message`, `exception message` vÃ  business error báº±ng tiáº¿ng Viá»‡t cÃ³ dáº¥u Ä‘á»ƒ dá»… Ä‘á»c vÃ  dá»… kiá»ƒm soÃ¡t.
- Ná»™i dung thÃ´ng bÃ¡o pháº£i ngáº¯n gá»n, rÃµ nghÄ©a, Ä‘Ãºng ngá»¯ cáº£nh nghiá»‡p vá»¥.
- TÃªn ká»¹ thuáº­t nhÆ° class, method, field, enum, báº£ng, cá»™t, API path hoáº·c error code váº«n giá»¯ nguyÃªn theo code gá»‘c khi cáº§n.
- KhÃ´ng log dá»¯ liá»‡u nháº¡y cáº£m hoáº·c thÃ´ng tin ná»™i bá»™ khÃ´ng cáº§n thiáº¿t.
- Message tráº£ ra API pháº£i thÃ¢n thiá»‡n vá»›i phÃ­a sá»­ dá»¥ng; message log ná»™i bá»™ cÃ³ thá»ƒ chi tiáº¿t hÆ¡n nhÆ°ng váº«n pháº£i rÃµ rÃ ng vÃ  an toÃ n.

## Quy táº¯c Repository vÃ  query
- Vá»›i CRUD cÆ¡ báº£n hoáº·c query ráº¥t Ä‘Æ¡n giáº£n theo convention, Æ°u tiÃªn dÃ¹ng `JpaRepository`.
- Vá»›i query cáº§n join, filter rÃµ rÃ ng, tá»‘i Æ°u SQL hoáº·c khÃ³ biá»ƒu diá»…n báº±ng JPA method name, Æ°u tiÃªn `native query`.
- KhÃ´ng dÃ¹ng `SELECT *` trong code.
- Query pháº£i tÆ°Æ¡ng thÃ­ch MySQL/MariaDB.
- Vá»›i query cÃ³ join phá»©c táº¡p vÃ  `pageable`, pháº£i cÃ³ `countQuery`.
- Khi map `native query` sang DTO hoáº·c projection, alias cá»™t pháº£i rÃµ rÃ ng vÃ  Ä‘Ãºng vá»›i field nháº­n dá»¯ liá»‡u.
- Khi so sÃ¡nh string giá»¯a cÃ¡c báº£ng, chÃº Ã½ collation nhÆ° `utf8mb4_unicode_ci`.
- KhÃ´ng tá»± Ã½ Ä‘á»•i cáº¥u trÃºc DB náº¿u user khÃ´ng yÃªu cáº§u.
- KhÃ´ng phÃ¡ vá»¡ mapping entity hiá»‡n cÃ³.
- KhÃ´ng táº¡o thÃªm query DB khÃ´ng cáº§n thiáº¿t; trÃ¡nh `N+1` náº¿u cÃ³ thá»ƒ xá»­ lÃ½ Ä‘Ãºng táº§ng.

## Quy táº¯c Entity vÃ  DTO
- KhÃ´ng expose entity trá»±c tiáº¿p náº¿u project Ä‘ang dÃ¹ng DTO.
- Khi thÃªm hoáº·c sá»­a field, pháº£i kiá»ƒm tra impact tá»›i entity, DTO, mapper, validation, response vÃ  query.
- Giá»¯ naming consistent vá»›i code hiá»‡n táº¡i.

## Quy táº¯c validation vÃ  dá»¯ liá»‡u
- LuÃ´n check null vá»›i input tá»« request, file import hoáº·c dá»¯ liá»‡u bÃªn ngoÃ i.
- String cáº§n Ä‘Æ°á»£c trim khi phÃ¹ há»£p vá»›i logic hiá»‡n táº¡i.
- Date/time pháº£i format rÃµ rÃ ng, khÃ´ng phá»¥ thuá»™c implicit locale.
- KhÃ´ng lÃ m máº¥t dá»¯ liá»‡u gá»‘c nhÆ° mÃ£ sinh viÃªn, mÃ£ há»“ sÆ¡, mÃ£ Ä‘á»‹nh danh.
- Khi query theo ngÃ y, chÃº Ã½ boundary Ä‘áº§u ngÃ y/cuá»‘i ngÃ y náº¿u logic nghiá»‡p vá»¥ cÃ³ liÃªn quan.

## Quy táº¯c báº£o máº­t
- KhÃ´ng bá» qua kiá»ƒm tra quyá»n hoáº·c role.
- Náº¿u role lÃ  `STUDENT`, chá»‰ Ä‘Æ°á»£c truy cáº­p dá»¯ liá»‡u cá»§a chÃ­nh mÃ¬nh, trá»« khi logic hiá»‡n táº¡i quy Ä‘á»‹nh khÃ¡c.
- KhÃ´ng bypass security chá»‰ Ä‘á»ƒ code cháº¡y.

## Quy táº¯c transaction
- Vá»›i cÃ¡c luá»“ng update nhiá»u báº£ng liÃªn quan nhÆ° `person`, `student`, `candidate`, pháº£i cÃ¢n nháº¯c `@Transactional`.
- KhÃ´ng lÃ m máº¥t tÃ­nh toÃ n váº¹n dá»¯ liá»‡u khi xáº£y ra lá»—i giá»¯a chá»«ng.

## Quy Æ°á»›c Java
- Giá»¯ tÃªn biáº¿n, method, class rÃµ nghÄ©a; khÃ´ng dÃ¹ng tÃªn 1 kÃ½ tá»±.
- KhÃ´ng thÃªm comment thá»«a; chá»‰ comment khi logic khÃ³ hiá»ƒu.
- Náº¿u Ä‘Ã£ cÃ³ pattern `service`, `repository`, `mapper`, `controller` sáºµn thÃ¬ pháº£i bÃ¡m theo.

## Kiá»ƒm tra sau khi sá»­a
- Sau khi sá»­a, Æ°u tiÃªn cháº¡y test hoáº·c build á»Ÿ pháº¡m vi nhá» nháº¥t cÃ³ thá»ƒ.
- Náº¿u khÃ´ng cháº¡y Ä‘Æ°á»£c test hoáº·c build, pháº£i nÃ³i rÃµ lÃ½ do.
- KhÃ´ng Ä‘Æ°a code chÆ°a cháº¯c cháº¡y Ä‘Æ°á»£c mÃ  khÃ´ng cáº£nh bÃ¡o.

## Quy táº¯c pháº£n há»“i
- Tráº£ lá»i ngáº¯n, táº­p trung vÃ o thay Ä‘á»•i chÃ­nh.
- Khi nÃªu file Ä‘Ã£ sá»­a, luÃ´n ghi rÃµ path file.
- Khi sá»­a code, náº¿u phÃ¹ há»£p thÃ¬ nÃªu ngáº¯n gá»n: nguyÃªn nhÃ¢n lá»—i, hÆ°á»›ng sá»­a vÃ  káº¿t quáº£.
- Náº¿u cÃ³ rá»§i ro, assumption hoáº·c trade-off, pháº£i nÃ³i rÃµ.

## KhÃ´ng Ä‘Æ°á»£c lÃ m
- KhÃ´ng tá»± Ã½ xÃ³a code cÅ© náº¿u user khÃ´ng yÃªu cáº§u.
- KhÃ´ng tá»± Ã½ commit.
- KhÃ´ng tá»± Ã½ thÃªm file docs, format toÃ n bá»™ project hoáº·c sá»­a rá»™ng hÆ¡n scope task náº¿u user khÃ´ng yÃªu cáº§u.
- KhÃ´ng refactor toÃ n bá»™ file hoáº·c class náº¿u chá»‰ cáº§n sá»­a nhá».

## Quy táº¯c tiáº¿ng Viá»‡t & encoding
- Viáº¿t tiáº¿ng Viá»‡t cÃ³ dáº¥u cho log, message.
- File pháº£i dÃ¹ng UTF-8 (khÃ´ng BOM).
- KhÃ´ng Ä‘á»ƒ kÃ½ tá»± lá»—i nhÆ° \ufeff, \uXXXX.
## Quy tac bo sung ve kiem tra tieng Viet sau khi sua
- Sau moi lan sua file co chuoi tieng Viet, phai ra soat lai tat ca chuoi vua sua de dam bao khong bi loi encoding.
- Phai kiem tra cac dau hieu loi nhu: ky tu bi vo dau, ky tu `?`, chuoi dang `\uXXXX`, chuoi mojibake nhu `TÃªn`, `MÃ£`, `thÃ nh cÃ´ng`.
- Neu phat hien loi encoding, phai sua ngay truoc khi ket thuc va dam bao file duoc luu UTF-8 khong BOM.