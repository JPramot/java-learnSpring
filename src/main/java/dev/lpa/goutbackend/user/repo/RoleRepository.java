package dev.lpa.goutbackend.user.repo;

import org.springframework.data.repository.CrudRepository;

import dev.lpa.goutbackend.user.models.Role;

//* Role หมายถึงให้ทำงานกับ class Role */
//* Integer หมายถึง มี id เป็น Integer */
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
