public class Customer {
    private String name;
    private String phone;
    private String eMail;

    public Customer(String name, String phone, String eMail) {
        if (name.equals(null)) {
            throw new NullPointerException("Вы не указали имя.");
        }

        if (phone.equals(null)) {
            throw new NullPointerException("Вы не указали номер телефона.");
        }

        if (eMail.equals(null)) {
            throw new NullPointerException("Вы не указали email.");
        }

        this.name = name;
        this.phone = phone;
        this.eMail = eMail;
    }

    public String toString() {
        return name + " - " + eMail + " - " + phone;
    }
}
