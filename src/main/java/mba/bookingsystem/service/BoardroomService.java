package mba.bookingsystem.service;

import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.repository.BoardroomRepository;
import mba.bookingsystem.util.RepositoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BoardroomService {

    private final BoardroomRepository boardroomRepository;

    @Autowired
    public BoardroomService(BoardroomRepository boardroomRepository) {
        this.boardroomRepository = boardroomRepository;
    }

    public List<Boardroom> getAll() {
        return boardroomRepository.findAll();
    }

    public Boardroom getOne(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Boardroom.class, boardroomRepository);
        return boardroomRepository.findByUuid(uuid);
    }

    public Boardroom create(Boardroom boardroom) {
        throwIfNameExists(boardroom.getName(), boardroom);
        boardroom = boardroomRepository.save(boardroom);
        return boardroom;
    }

    public Boardroom update(Boardroom boardroom, UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Boardroom.class, boardroomRepository);
        Boardroom dbBoardroom = boardroomRepository.findByUuid(uuid);
        throwIfNameExists(boardroom.getName(), boardroom);
        dbBoardroom.setName(boardroom.getName());
        boardroom = boardroomRepository.save(boardroom);
        return dbBoardroom;

    }

    public void delete(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, boardroomRepository);
        Boardroom boardroom = boardroomRepository.findByUuid(uuid);
        boardroomRepository.delete(boardroom);
    }

    private void throwIfNameExists(String name, Boardroom boardroom) {
        Boardroom dbBoardroom = boardroomRepository.findByName(name);
        if (dbBoardroom == null) {
            return;
        }
        if (boardroom != null) {
            if (boardroom.getName().equals(dbBoardroom.getName()) && !boardroom.getUuid().equals(dbBoardroom.getUuid())) {
                throw new AlreadyExistsException(name);
            }
        }
    }
}
