import com.admtel.telephonyserver.core.ChannelData;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.interfaces.ScriptFactory

import groovy.sql.Sql

class AdmScriptFactory implements ScriptFactory{
	@Override
	public Script createScript (ChannelData channelData){
		def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/adm_appserver", "tester",
			"tester1234", "org.postgresql.Driver")
		
		def calledNumber = channelData.getCalledNumber()
		println "Finding script for ${calledNumber}"
		def row = sql.firstRow("select * from voice_service_number as vsn, application as app where vsn.number = ? and app.id=vsn.application_id",[calledNumber])
		
		if (row){
			println " ******** Loading class"
			return SmartClassLoader.createInstance(Script.class, row.script)
		}
	}
}