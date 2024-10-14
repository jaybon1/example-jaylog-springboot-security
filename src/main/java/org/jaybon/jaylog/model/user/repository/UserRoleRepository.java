package org.jaybon.jaylog.model.user.repository;

import org.jaybon.jaylog.model.user.constraint.RoleType;
import org.jaybon.jaylog.model.user.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findByUserEntity_Id(Long id);

    UserRoleEntity findByUserEntity_IdAndRole(Long userEntity_id, RoleType role);

}
