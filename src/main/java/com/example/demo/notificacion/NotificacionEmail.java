import com.example.demo.interface.IEstrategiaNotificacion;
import com.example.demo.interface.IAdapterEmail;
import com.example.demo.factory.EmailAdapterFactory;
import com.example.demo.mailUserTest.Usuario;

public class NotificacionEmail implements IEstrategiaNotificacion {

    private final EmailAdapterFactory factory;

    public NotificacionEmail(EmailAdapterFactory factory) {
        this.factory = factory;
    }

    @Override
    public void notificar(Usuario usuario, String mensaje) {
        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            System.out.println("Usuario sin email. No se envía.");
            return;
        }
        IAdapterEmail adapter = factory.getAdapter(usuario.getEmail());
        String asunto = "Mensaje para " + usuario.getUsername();
        String cuerpo = mensaje;
        adapter.enviarEmail(usuario.getEmail(), asunto, cuerpo);
    }
}
