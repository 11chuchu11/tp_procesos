public class AdapterJavaPush implements IAdapterPush{
    @Override
    public void enviarPush(String destinatario, String mensaje) {
        System.out.println("Notificación Push enviada a " + destinatario + ": " + mensaje);
    }

}
