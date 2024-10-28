package dev.lpa.goutbackend.user;

import org.springframework.data.repository.CrudRepository;

//* Role หมายถึงให้ทำงานกับ class Role */
//* Integer หมายถึง มี id เป็น Integer */
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
