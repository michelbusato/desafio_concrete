package br.com.concrete.desafio.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final Integer MEIA_HORA = 30;

	public static Date dataAdiantadaMeiaHora(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, MEIA_HORA);
		return cal.getTime();

	}
	
	public static Date dataAtual(){
		return new Date();
	}

}
