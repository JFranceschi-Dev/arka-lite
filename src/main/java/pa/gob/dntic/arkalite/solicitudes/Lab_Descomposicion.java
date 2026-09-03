package pa.gob.dntic.arkalite.solicitudes;

import java.util.*;
import java.util.function.Consumer;
import java.lang.reflect.*;

/*
 * ============================================================
 *  LAB 12 (Sesión 24) — Endurecer la descomposición  [DIFÍCIL]
 *  Programa Upskilling Backend Java · DNTIC
 * ============================================================
 *  SEIS compuertas. Todas obligatorias. Empiezas en 2/6.
 *  Ninguna se aprueba acoplando los servicios: si Solicitudes conoce a
 *  Notificaciones (o al revés), la compuerta de frontera te REPRUEBA.
 *
 *  LO QUE DEBES LOGRAR:
 *   (3) Evento SolicitudAprobada: aprobar(id) lo publica; Notificaciones reacciona.
 *   (4) Evento SolicitudRechazada: rechazar(id) lo publica; Notificaciones reacciona.
 *   (5) Resiliencia: un consumidor que LANZA una excepción NO debe tumbar al
 *       publicador ni impedir que los demás consumidores reciban el evento.
 *   (6) El aviso de "enviada" debe incluir el DEPARTAMENTO de la solicitud...
 *       PERO Notificaciones NO puede conocer a Solicitudes para obtenerlo.
 *       (¿De dónde saca el dato sin preguntarle al otro servicio?)
 *
 *  Corre:  java Lab_Descomposicion.java
 * ============================================================
 */
public class Lab_Descomposicion {
    public static void main(String[] args) {
        int s = 0;
        s += g1_enviadaFluye();
        s += g2_fronteraSolicitudes();
        s += g3_aprobadaFluye();
        s += g4_rechazadaFluye();
        s += g5_resiliencia();
        s += g6_departamentoSinAcople();
        System.out.println("\nPUNTAJE: " + s + "/6");
        System.out.println(s == 6 ? "Descomposición endurecida. Sobreviviste."
                                  : "Aún no. Ninguna compuerta se aprueba con acople.");
    }

    static Bus busCon(ServicioDeNotificaciones notis) {
        Bus bus = new Bus();
        bus.suscribir(notis::manejar);
        return bus;
    }

    static int g1_enviadaFluye() {
        try {
            ServicioDeNotificaciones n = new ServicioDeNotificaciones();
            ServicioDeSolicitudes s = new ServicioDeSolicitudes(busCon(n));
            s.registrar("INC-1", "Incidente", "TI");
            s.enviar("INC-1");
            if (n.total() == 1) { ok("(1) SolicitudEnviada -> notificación"); return 1; }
            fail("(1) enviar no produjo notificación"); return 0;
        } catch (Throwable t) { fail("(1) " + t); return 0; }
    }

    static int g2_fronteraSolicitudes() {
        for (Field f : ServicioDeSolicitudes.class.getDeclaredFields())
            if (f.getType().getSimpleName().toLowerCase().contains("notificacion")) {
                fail("(2) FRONTERA: Solicitudes referencia a " + f.getType().getSimpleName()); return 0;
            }
        ok("(2) frontera: Solicitudes no conoce a Notificaciones"); return 1;
    }

    static int g3_aprobadaFluye() {
        try {
            ServicioDeNotificaciones n = new ServicioDeNotificaciones();
            ServicioDeSolicitudes s = new ServicioDeSolicitudes(busCon(n));
            s.registrar("INC-2", "Incidente", "TI");
            ServicioDeSolicitudes.class.getDeclaredMethod("aprobar", String.class).invoke(s, "INC-2");
            if (n.contiene("aprob")) { ok("(3) SolicitudAprobada -> notificación"); return 1; }
            fail("(3) aprobar no produjo notificación de aprobación"); return 0;
        } catch (NoSuchMethodException e) { fail("(3) falta aprobar(String) que publique SolicitudAprobada"); return 0; }
        catch (Throwable t) { fail("(3) " + causa(t)); return 0; }
    }

    static int g4_rechazadaFluye() {
        try {
            ServicioDeNotificaciones n = new ServicioDeNotificaciones();
            ServicioDeSolicitudes s = new ServicioDeSolicitudes(busCon(n));
            s.registrar("INC-3", "Incidente", "TI");
            ServicioDeSolicitudes.class.getDeclaredMethod("rechazar", String.class).invoke(s, "INC-3");
            if (n.contiene("rechaz")) { ok("(4) SolicitudRechazada -> notificación"); return 1; }
            fail("(4) rechazar no produjo notificación de rechazo"); return 0;
        } catch (NoSuchMethodException e) { fail("(4) falta rechazar(String) que publique SolicitudRechazada"); return 0; }
        catch (Throwable t) { fail("(4) " + causa(t)); return 0; }
    }

    static int g5_resiliencia() {
        try {
            ServicioDeNotificaciones n = new ServicioDeNotificaciones();
            Bus bus = new Bus();
            bus.suscribir(e -> { throw new RuntimeException("consumidor malo"); });  // revienta
            bus.suscribir(n::manejar);                                                // debe recibir igual
            ServicioDeSolicitudes s = new ServicioDeSolicitudes(bus);
            s.registrar("INC-4", "Incidente", "TI");
            s.enviar("INC-4");
            if (n.total() == 1) { ok("(5) resiliencia: un consumidor que falla no tumba a los demás"); return 1; }
            fail("(5) el consumidor bueno no recibió el evento"); return 0;
        } catch (Throwable t) { fail("(5) el fallo de un consumidor tumbó al publicador -> bus no resiliente"); return 0; }
    }

    static int g6_departamentoSinAcople() {
        ServicioDeNotificaciones n = new ServicioDeNotificaciones();
        ServicioDeSolicitudes s = new ServicioDeSolicitudes(busCon(n));
        s.registrar("INC-5", "Incidente", "Tesorería");
        s.enviar("INC-5");
        boolean incluye = n.contiene("Tesorería");
        boolean acoplado = false;
        for (Field f : ServicioDeNotificaciones.class.getDeclaredFields())
            if (f.getType().getSimpleName().equals("ServicioDeSolicitudes")) acoplado = true;
        if (incluye && !acoplado) { ok("(6) departamento en el aviso, sin acoplar a Solicitudes"); return 1; }
        if (acoplado) fail("(6) FRONTERA: Notificaciones referencia a ServicioDeSolicitudes. El dato va en el EVENTO.");
        else fail("(6) el aviso de enviada no incluye el departamento");
        return 0;
    }

    static void ok(String m){ System.out.println("[ OK ] " + m); }
    static void fail(String m){ System.out.println("[FALLA] " + m); }
    static String causa(Throwable t){ return t.getCause()==null? String.valueOf(t): String.valueOf(t.getCause()); }
}

// ============================================================
//  TU ÁREA DE TRABAJO
// ============================================================
record SolicitudEnviada(String id, String tipo) {}        // TODO(6): agrega el departamento
// TODO(3): record SolicitudAprobada(String id) {}
// TODO(4): record SolicitudRechazada(String id) {}

class Bus {   // TODO(5): hazlo resiliente
    private final List<Consumer<Object>> suscriptores = new ArrayList<>();
    void suscribir(Consumer<Object> c) { suscriptores.add(c); }
    void publicar(Object evento) {
        for (Consumer<Object> x : suscriptores) x.accept(evento);   // ingenuo: si uno lanza, se cae todo
    }
}

class ServicioDeSolicitudes {   // publica; NO conoce a Notificaciones
    private final Map<String,String[]> datos = new LinkedHashMap<>();   // id -> [estado, tipo, departamento]
    private final Bus bus;
    ServicioDeSolicitudes(Bus bus) { this.bus = bus; }
    void registrar(String id, String tipo, String departamento) {
        datos.put(id, new String[]{"BORRADOR", tipo, departamento});
    }
    void enviar(String id) {
        String[] d = datos.get(id); d[0] = "ENVIADA";
        bus.publicar(new SolicitudEnviada(id, d[1]));   // TODO(6): incluir el departamento d[2]
    }
    // TODO(3): void aprobar(String id) { datos.get(id)[0]="APROBADA"; bus.publicar(new SolicitudAprobada(id)); }
    // TODO(4): void rechazar(String id) { datos.get(id)[0]="RECHAZADA"; bus.publicar(new SolicitudRechazada(id)); }
}

class ServicioDeNotificaciones {   // reacciona; NO conoce a Solicitudes
    private final List<String> avisos = new ArrayList<>();
    void manejar(Object evento) {
        if (evento instanceof SolicitudEnviada e)
            avisos.add("Enviada " + e.id());   // TODO(6): incluir el departamento (debe venir en el evento)
        // TODO(3): else if (evento instanceof SolicitudAprobada a) avisos.add("Aprobada " + a.id());
        // TODO(4): else if (evento instanceof SolicitudRechazada r) avisos.add("Rechazada " + r.id());
    }
    int total() { return avisos.size(); }
    boolean contiene(String frag) { return avisos.stream().anyMatch(x -> x.toLowerCase().contains(frag.toLowerCase())); }
}
