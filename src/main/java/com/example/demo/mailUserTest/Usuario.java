public class Usuario {
    private final String username;
    private String password;
    private String email;

    public Usuario(String username) {
        this.username = username;
    }

    public Usuario(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
}
