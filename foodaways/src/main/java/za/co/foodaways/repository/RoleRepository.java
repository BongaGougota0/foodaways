package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findRoleByRoleName(String roleName);
    Roles findRoleByRoleId(Integer roleId);
}
