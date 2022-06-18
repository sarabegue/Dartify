package com.mentit.dartify.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.TimeZone;

public class FormatUtil {
    public FormatUtil() {
    }

    public static String getTiempo(String fechaInicio) {
        String resultado = "";
        if (fechaInicio == null || fechaInicio == null) return "";

        fechaInicio = fechaInicio.replace(" ", "T");

        try {
            DateTimeZone zone = DateTimeZone.forID("America/Mexico_City");
            DateTime then = new DateTime(fechaInicio, zone);
            DateTime now = DateTime.now(zone);
            Period period = new Period(then, now);

            if (period.getMonths() == 1) {
                resultado = "Hace" + " 1 " + "mes";
            } else if (period.getMonths() > 1) {
                resultado = "Hace" + " " + period.getMonths() + " " + "meses";
            } else if (period.getWeeks() == 1) {
                resultado = "Hace" + " 1 " + "semana";
            } else if (period.getWeeks() > 1) {
                resultado = "Hace" + " " + period.getWeeks() + " " + "semanas";
            } else if (period.getDays() == 1) {
                resultado = "Ayer";
            } else if (period.getDays() > 0) {
                resultado = "Hace" + " " + period.getDays() + " " + "días";
            } else if (period.getHours() == 1) {
                resultado = "Hace" + " 1 " + "hora";
            } else if (period.getHours() > 1) {
                resultado = "Hace" + " " + period.getHours() + " " + "horas";
            } else if (period.getMinutes() == 1) {
                resultado = "Hace" + " 1 " + "minuto";
            } else if (period.getMinutes() > 1) {
                resultado = "Hace" + " " + period.getMinutes() + " " + "minutos";
            } else if (period.getMinutes() < 1) {
                resultado = "Ahorita";
            } else {
                resultado = fechaInicio;
            }
        } catch (Exception w) {
        }
        return resultado;
    }

    public static String getHora(String fecha) {
        if (fecha == null || fecha == null) return "";

        fecha = fecha.replace("T", " ");
        return fecha.substring(11, 16);
    }

    public static int getEdad(String fecha) {
        if (fecha == null || fecha.equals("null")) return 0;

        fecha = fecha.replace("T", " ");

        if (fecha.length() == 10) {
            fecha = fecha + " 00:00:00";
        }

        TimeZone tt = TimeZone.getDefault();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime inicio = formatter.parseDateTime(fecha);
        DateTime fin = new DateTime();
        Long itmp = fin.getMillis() + tt.getRawOffset() + tt.getDSTSavings();
        fin = new DateTime(itmp);

        Interval intervalo = new Interval(inicio, fin);
        Period period = intervalo.toPeriod();

        return period.getYears();
    }

    public static String getChatRoom(long userid1, long userid2) {
        long[] b = new long[]{userid1, userid2};
        Arrays.sort(b);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
            if (i != b.length - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
