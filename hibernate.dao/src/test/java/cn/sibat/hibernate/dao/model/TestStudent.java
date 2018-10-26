package cn.sibat.hibernate.dao.model;


import javax.persistence.*;

import cn.sibat.model.Pojo;


@Entity
@Table(name="t_student")
public class TestStudent extends Pojo{
    public TestStudent(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public TestStudent() {
        
    }

    private String name;
    private String address;
    private int age;
    /**
     *classroom对象的外键，不建议使用对象的方式关联 
     */
    private int cid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", address=" + address + ", age=" + age + ", cid=" + cid + ", id="
				+ getId() + "]";
	}


	
	
}
