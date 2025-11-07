import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import com.example.demo.adapter.AdapterHotmail;
import com.example.demo.adapter.AdapterJakartaMail;
import com.example.demo.interface.IAdapterEmail;
import com.example.demo.smtp.SmtpConfig;

public class EmailAdapterFactory {

    private final Map<String, Supplier<IAdapterEmail>> adaptersPorDominio = new HashMap<>();
    private final Supplier<IAdapterEmail> adapterDefault;

    public EmailAdapterFactory(SmtpConfig gmailConfig) {
        // default
        this.adapterDefault = () -> new AdapterJakartaMail(gmailConfig);

        // consola
        adaptersPorDominio.put("hotmail.com", AdapterHotmail::new);
        adaptersPorDominio.put("hotmail.es", AdapterHotmail::new);
        adaptersPorDominio.put("outlook.com", AdapterHotmail::new);
        adaptersPorDominio.put("live.com", AdapterHotmail::new);
        adaptersPorDominio.put("msn.com", AdapterHotmail::new);
    }

    public IAdapterEmail getAdapter(String email) {
        String dominio = extraerDominio(email);
        Supplier<IAdapterEmail> proveedor = adaptersPorDominio.getOrDefault(dominio, adapterDefault);
        return proveedor.get();
    }

    private String extraerDominio(String email) {
        if (email == null) return "";
        int at = email.indexOf('@');
        if (at < 0 || at == email.length() - 1) return "";
        return email.substring(at + 1).toLowerCase();
    }
}