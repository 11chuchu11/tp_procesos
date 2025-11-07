import com.example.demo.interface.IEstrategiaNotificacion;
import com.example.demo.interface.IAdapterPush;
import com.example.demo.adapter.AdapterJavaPush;
import com.example.demo.mailUserTest.Usuario;

public class NotificacionPush implements IEstrategiaNotificacion{


    public NotificacionPush(AdapterJavaPush adapterJavaPush) {

    }

    @Override
    public void notificar(Usuario usuario, String mensaje) {

    }


}
