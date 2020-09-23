# obss-mentorship
An in-company mentorship system where users can mentor a collegue, while they can also be mentored by another. This project was developed during my internship @ OBSS (https://obss.com.tr/) in August 2020.

## Project Description
The application consists of 2 different profiles: Regular User and Admin. Users can log in via LinkedIn, Google, and LDAP Authentication, and their profile data is also fetched from LinkedIn or Google API if logged in via those. When a regular user is logged in to the system, they can view the ongoing/finished mentorships (either they are mentor or mentee), apply for being a mentor, view ongoing mentor applications, search for available mentors (by name/topic/free text search). 

Admins are authenticated only via LDAP authentication, Admins can add/remove available mentorship topics & subtopics, view/remove current mentors in system, accept/decline mentorship applications.

When a deadline of a phase in a mentorship is within 1 hour, system automatically sends e-mail to the mentor and the mentee, and an e-mail is also sent upon successfully terminating a phase of a mentorship.

During the mentor searching, system also offers a free text search based on the experiences of available mentors (which they have indicated while applying as a mentor), delivered by SOLR.

## Technologies used

### Frontend

* Bootstrap
* CSS
* Thymeleaf
* NPM
* Sass
* JavaScript

### Backend

* Spring Boot
* Hibernate
* LDAP
* JSON
* SOLR

### Database

* MySQL
