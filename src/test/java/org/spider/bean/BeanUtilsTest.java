package org.spider.bean;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

public class BeanUtilsTest {

    public static class Person {
        private String name;
        private int    age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class Employee extends Person {
        private String department;

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String quest) {
            this.department = quest;
        }

        public static class Builder implements Supplier<Employee> {
            private String name;
            private int    age;
            private String department;

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withAge(int age) {
                this.age = age;
                return this;
            }

            public Builder withDepartment(String department) {
                this.department = department;
                return this;
            }

            @Override
            public Employee get() {
                Employee emp = new Employee();
                emp.setName(name);
                emp.setAge(age);
                emp.setDepartment(department);
                return emp;
            }
        }
    }

    @Test public void mapperTest() {
        Person person1 = new Person();
        person1.setName("BomBom");
        person1.setAge(40);
        Person person2 = Mapper.from(Person::getAge).to(Person::setAge)
            .andFrom(Person::getName).to(Person::setName)
            .creatingWith(Person::new)
            .apply(person1);
        Assert.assertTrue(person2.getName().equals("BomBom"));
        Assert.assertTrue(person2.getAge() == 40);

        Employee emp = Mapper.from(Person::getAge).to(Employee::setAge)
            .andFrom(Getter.ofNullable(Person::getName).orElse("admin")).to(Employee::setName)
            .andFrom("admin").to(Employee::setDepartment)
            .creatingWith(Employee::new)
            .apply(person1);

        Assert.assertTrue(emp.getName().equals("BomBom"));
        Assert.assertTrue(emp.getDepartment().equals("admin"));
        Assert.assertTrue(emp.getAge() == 40);
    }

}
