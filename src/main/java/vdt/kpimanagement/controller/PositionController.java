package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.PositionRequest;
import vdt.kpimanagement.dto.PositionResponse;
import vdt.kpimanagement.entity.Position;
import vdt.kpimanagement.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController extends BaseController<Position, PositionRequest, PositionResponse, Long> {

    public PositionController(PositionService positionService) {
        super(positionService, "chức vụ");
    }
}
