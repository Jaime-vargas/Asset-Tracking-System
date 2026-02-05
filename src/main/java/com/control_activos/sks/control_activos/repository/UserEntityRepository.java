package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.enums.UserRoleEnum;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
}
