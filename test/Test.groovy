import groovy.sql.Sql;
import org.groodbc.driver.*;



def sql = Sql.newInstance('groodbc:org: new groovy.json.JsonSlurper().parse( new File("./test/data/test.json").newReader("UTF-8") )', '', '', 'org.groodbc.driver.GDBDriver')

sql.eachRow(' data.menu.popup.menuitem ') { row ->
	println "${row}"
}

def hello='world'

sql.eachRow(" println(param[1]); data.menu.popup.menuitem ", ['world!'] ) { row ->
	println "${row}"
}
