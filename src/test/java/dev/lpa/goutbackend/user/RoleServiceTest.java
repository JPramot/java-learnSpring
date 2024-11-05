package dev.lpa.goutbackend.user;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.lpa.goutbackend.commons.enumulation.RoleEnum;


@ExtendWith(MockitoExtension.class) //* บอกว่าใช้ Mockito เป็น extension ของ JUnit
class RoleServiceTest {

    @InjectMocks //* ให้ inject mock เข้าไปใน RoleService ที่ถูกสร้าง
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository; //* สร้าง mock ของ RoleRepository

    @Test //* test method
    void shouldReturnRole() {
        List<Role> roles = List.of(
            new Role(RoleEnum.CONSUMER.getId(),RoleEnum.CONSUMER.name()),
            new Role(RoleEnum.ADMIN.getId(),RoleEnum.ADMIN.name()),
            new Role(RoleEnum.COMPANY.getId(),RoleEnum.COMPANY.name())
        );
        when(roleRepository.findAll()).thenReturn(roles); //* เมื่อมีการเรียก method findAll ให้คืนค่า roles

        var actual = roleService.getAllRoles(); //* ให้เรียก method getAllRoles -> findAll จาก RoleRepository
        List<Role> result = new ArrayList<>();
        actual.iterator().forEachRemaining(result::add);

        Assertions.assertEquals(3, result.size()); //* ตรวจสอบว่า result มีค่าเท่ากับ 3
    }
}
