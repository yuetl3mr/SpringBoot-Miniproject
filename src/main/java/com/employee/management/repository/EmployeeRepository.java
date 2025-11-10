package com.employee.management.repository;

import com.employee.management.dto.DepartmentStatistics;
import com.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT e FROM Employee e WHERE e.department.name LIKE %:departmentName%")
    List<Employee> findByDepartmentNameContaining(@Param("departmentName") String departmentName);
    
    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:name% OR e.department.name LIKE %:departmentName%")
    List<Employee> findByNameOrDepartmentName(@Param("name") String name, @Param("departmentName") String departmentName);

    @Query("SELECT new com.employee.management.dto.DepartmentStatistics(d.name, COUNT(e.id)) " +
           "FROM Employee e JOIN e.department d " +
           "GROUP BY d.name " +
           "ORDER BY COUNT(e.id) DESC")
    List<DepartmentStatistics> getEmployeeCountByDepartment();

    @Query("SELECT COUNT(e) FROM Employee e")
    Long getTotalEmployeeCount();
}
