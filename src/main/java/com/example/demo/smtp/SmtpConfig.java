public class SmtpConfig {
    private final String host;
    private final int port;
    private final String username; // null si no requiere auth
    private final String password; // password de API
    private final boolean startTls; // puerto 587
    private final boolean ssl;      // puerto 465
    private final String from;      // remitente
    private final boolean debug;    // true para ver el intercambio SMTP
    private final boolean html;     // envío de HTML

    public SmtpConfig(
            String host, int port,
            String username, String password,
            boolean startTls, boolean ssl,
            String from, boolean debug, boolean html
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.startTls = startTls;
        this.ssl = ssl;
        this.from = from;
        this.debug = debug;
        this.html = html;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isStartTls() { return startTls; }
    public boolean isSsl() { return ssl; }
    public String getFrom() { return from; }
    public boolean isDebug() { return debug; }
    public boolean isHtml() { return html; }
}