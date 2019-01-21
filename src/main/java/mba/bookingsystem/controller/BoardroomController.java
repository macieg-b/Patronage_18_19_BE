package mba.bookingsystem.controller;

import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.view.BoardroomView;
import mba.bookingsystem.service.BoardroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static mba.bookingsystem.util.ModelMapper.convertToModel;
import static mba.bookingsystem.util.ModelMapper.convertToView;


@RestController
@RequestMapping(value = RestUrl.BOARDROOM_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardroomController {

    private final BoardroomService boardroomService;

    @Autowired
    public BoardroomController(BoardroomService boardroomService) {
        this.boardroomService = boardroomService;
    }

    @GetMapping
    public ResponseEntity<List<BoardroomView>> getAll() {
        var boardroomList = boardroomService.getAll();
        return ResponseEntity
                .ok(convertToView(boardroomList, BoardroomView.class));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<BoardroomView> getOne(@PathVariable UUID uuid) {
        var boardroom = boardroomService.getOne(uuid);
        return ResponseEntity
                .ok(convertToView(boardroom, BoardroomView.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardroomView> create(@Valid @RequestBody BoardroomView boardroomView) {
        var boardroom = boardroomService.create(convertToModel(boardroomView, Boardroom.class));
        return ResponseEntity
                .created(URI.create(
                        String.format("/%s/%s",
                                ServletUriComponentsBuilder.fromCurrentRequest().build().toUri(),
                                boardroom.getUuid())
                        )
                )
                .body(convertToView(boardroom, BoardroomView.class));
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<BoardroomView> update(@Valid @RequestBody BoardroomView boardroomView, @PathVariable UUID uuid) {
        var boardroom = boardroomService.update(convertToModel(boardroomView, Boardroom.class), uuid);
        return ResponseEntity
                .ok(convertToView(boardroom, BoardroomView.class));

    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        boardroomService.delete(uuid);
        return ResponseEntity
                .ok()
                .build();

    }
}
