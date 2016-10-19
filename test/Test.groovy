import groovy.sql.Sql;
import org.groodbc.driver.*;



//def sql = Sql.newInstance('groodbc:org: new groovy.json.JsonSlurper().parse( new File("./test/data/test.json").newReader("UTF-8") )', '', '', 'org.groodbc.driver.GDBDriver')
def con = GDBConnection.eval( 'groodbc:org: new groovy.json.JsonSlurper().parse( new File(FILENAME).newReader("UTF-8") )', [FILENAME:"./test/data/test.json"] )
def sql = Sql.newInstance(con)

sql.eachRow('select = { ROOT.menu.popup.menuitem }') { row ->
	println "${row}"
}

println "-------------------------------------"

def p = sql.getConnection().prepareStatement("select = {String aaa-> println(aaa); ROOT.menu.popup.menuitem } ")

println p.getParameterMetaData()
p.setString(1,"hello world!")
def r = p.executeQuery();
println r.getMetaData()




println "-------------------------------------"

sql.eachRow('select = {String p_value-> ROOT.menu.popup.menuitem.findAll{ it.value == p_value } }',['Open']) { row ->
	println "${row}"
}

println "-------------------------------------"

sql.eachRow('''select = {->
	[
	  [
		'COUNT'     :ROOT.menu.popup.menuitem.size(),
		'MAX_VALUE' :ROOT.menu.popup.menuitem.collect{it.value}.max()
	  ] 
	]
}''') { row ->
	println "${row}"
}
