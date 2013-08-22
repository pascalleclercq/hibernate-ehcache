package com.developer.article.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity @XmlRootElement(name="Person") @XmlAccessorType(XmlAccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@XmlElement private Integer id;
	@XmlElement private String firstName;
	@XmlElement private String lastName;
	@XmlElement private String email;
	@Version @XmlTransient
	private Integer version;

	public Person() {}
	
	public Person(Integer id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object obj) {
		//reflection acceptable for testing only, in production use Lombok
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		//reflection acceptable for testing only, in production use Lombok
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		//reflection acceptable for testing only, in production use Lombok
		return ToStringBuilder.reflectionToString(this);
	}
	
}
