import java.util.List;
import com.example.demo.interface.IEstrategiaNotificacion;
import com.example.demo.mailUserTest.Usuario;

public class Notificador {
    private List<IEstrategiaNotificacion> estrategias;

    public Notificador(List<IEstrategiaNotificacion> estrategias) {
        this.estrategias = estrategias;
    }

    public void setEstrategias(List<IEstrategiaNotificacion> estrategias) {
        this.estrategias = estrategias;
    }

    public void notificar(Usuario usuario, String mensaje){
        for (IEstrategiaNotificacion estrategia : estrategias){
            estrategia.notificar(usuario, mensaje);
        }
    }
}
