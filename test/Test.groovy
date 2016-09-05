import groovy.sql.Sql;
import org.groodbc.driver.*;



def sql = Sql.newInstance('groodbc:org: new groovy.json.JsonSlurper().parse( new File("./test/data/test.json").newReader("UTF-8") )', '', '', 'org.groodbc.driver.GDBDriver')

sql.eachRow('select = { data.menu.popup.menuitem }') { row ->
	println "${row}"
}

def hello='world'

sql.eachRow('''
select = {String aaa 
	println aaa; 
	data.menu.popup.menuitem 
} ''', ['hello world!'] ) { row ->

	println "${row}"
}



def p = sql.getConnection().prepareStatement("select = {String aaa-> println(aaa); data.menu.popup.menuitem } ")

println p.getParameterMetaData()

def r = p.executeQuery();
println r.getMetaData()
