package org.nuxeo.usermapper.test.dummy;

public class DummyUser {

    String login;

    Name name;

    public DummyUser(String login, String fname, String lname) {
        this.login = login;
        name = new Name();
        name.firstName = fname;
        name.lastName = lname;
    }

    public class Name {

        String firstName;

        String lastName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

    }

    public String getLogin() {
        return login;
    }

    public Name getName() {
        return name;
    }

}
