package com.geoly.app.repositories;

import com.geoly.app.models.Role;
import com.geoly.app.models.RoleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleList name);

    Optional<Set<Role>> findAlLByIdIn(List<Integer> ids);
}
