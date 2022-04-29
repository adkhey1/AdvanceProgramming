package com.example.advanceprogramming.auth.model;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    //Model for User
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name="lastName",nullable = false)
    private String lastName;
    /*


        //ignore
        @OneToMany
        @CollectionTable(name = "active_surveys ", joinColumns = @JoinColumn(name = "id"))
        @Column(name = "actSurv", nullable = false)
        private List<Survey> activeSurveys = new ArrayList<>();

        public void addSurvey(Survey survey){
            activeSurveys.add(survey);
        }
    */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +

                '}';
    }
}
