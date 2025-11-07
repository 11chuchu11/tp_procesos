public class AdapterHotmail implements IAdapterEmail {
    @Override
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        System.out.println("HOTMAIL -> to: " + destinatario + " | asunto: " + asunto + " | cuerpo: " + cuerpo);
    }
}