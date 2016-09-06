import groovy.sql.Sql;
import org.groodbc.driver.*;



def sql = Sql.newInstance('groodbc:org: new groovy.json.JsonSlurper().parse( new File("./test/data/test.json").newReader("UTF-8") )', '', '', 'org.groodbc.driver.GDBDriver')

sql.eachRow('select = { data.menu.popup.menuitem }') { row ->
	println "${row}"
}

println "-------------------------------------"

def p = sql.getConnection().prepareStatement("select = {String aaa-> println(aaa); data.menu.popup.menuitem } ")

println p.getParameterMetaData()
p.setString(1,"hello world!")
def r = p.executeQuery();
println r.getMetaData()




println "-------------------------------------"

sql.eachRow('select = {String p_value-> data.menu.popup.menuitem.findAll{ it.value == p_value } }',['Open']) { row ->
	println "${row}"
}

println "-------------------------------------"

sql.eachRow('''select = {->
	[
	  [
		'COUNT'     :data.menu.popup.menuitem.size(),
		'MAX_VALUE' :data.menu.popup.menuitem.collect{it.value}.max()
	  ] 
	]
}''') { row ->
	println "${row}"
}
