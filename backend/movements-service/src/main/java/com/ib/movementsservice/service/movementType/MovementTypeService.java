package com.ib.movementsservice.service.movementType;

import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.repository.MovementTypeRepository;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovementTypeService implements IMovementTypeService{

    private final MovementTypeRepository movementTypeRepository;

    @Autowired
    public MovementTypeService(MovementTypeRepository movementTypeRepository){
        this.movementTypeRepository = movementTypeRepository;
    }

    @Override
    public Object obtainAll() {
        return movementTypeRepository.findAll();
    }

    @Override
    public Optional<MovementType> obtainSpecificMovementType(int id) {
        return movementTypeRepository.findById(id);
    }

    @Override
    public MovementType createMovementType(MovementType type) {
        return movementTypeRepository.save(type);
    }

    @Override
    public Response<Statuses.UpdateMovementTypeStatus, MovementType> updateMovementType(int id, MovementType type) {
        type.setId(id);
        Optional<MovementType> originalType = movementTypeRepository.findById(id);
        if(originalType.isEmpty()) return new Response<>(Statuses.UpdateMovementTypeStatus.NOT_FOUND,null);
        return new Response<>(Statuses.UpdateMovementTypeStatus.SUCCESS,movementTypeRepository.save(type));
    }

    @Override
    public Response<Statuses.HardDeleteMovementTypeStatus, MovementType> hardDeleteMovementType(int id) {
        if(!movementTypeRepository.existsById(id)) return new Response<>(Statuses.HardDeleteMovementTypeStatus.NOT_FOUND,null);
        movementTypeRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteMovementTypeStatus.SUCCESS,null);
    }
}
