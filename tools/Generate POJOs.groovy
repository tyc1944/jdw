import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.text.SimpleDateFormat

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 *   <p>
      自动生成pojo，结合lombok swagger annotation
      如果数据库字段类型是enum ，生成默认枚举类型EnumName
 *   </p>
 */

// get automatic folder according to the selected file
packageName = ""
typeMapping = [
        (~/(?i)tinyint|bool|boolean/)            : "Boolean",
        (~/(?i)bigint/)                          : "Long",
        (~/(?i)smallint|mediumint|int/)          : "Integer",
        (~/(?i)float|double|decimal|real/)       : "Double",
        (~/(?i)datetime|timestamp/)              : "Instant",
        (~/(?i)date/)                            : "java.sql.Date",
        (~/(?i)time/)                            : "java.sql.Time",
        (~/(?i)blob|binary|bfile|clob|raw|image/): "InputStream",
        (~/(?i)enum/)                            : "EnumName",
        (~/(?i)/)                                : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable /*&& it.getKind() == ObjectKind.TABLE*/ }.each {
        generate(it, dir)
    }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    packageName = getPackageName(dir)
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, className + ".java")), "UTF-8"))
    printWriter.withPrintWriter { out -> generate(out, table, className, fields) }
    // Chinese comments are garbled
    // new File(dir, className + ".java").withPrintWriter { out -> generate(out, table, className, fields) }
}


// Get the path to the folder where the package
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}


def generate(out, table, className, fields) {
    def tableName = table.getName()
    def tableComment = table.getComment()

    out.println "package $packageName"
    out.println ""
    out.println "import lombok.*;"
    out.println "import javax.persistence.*;"
    out.println "import com.yunmo.domain.common.Audited;"
    out.println "import io.swagger.v3.oas.annotations.media.Schema;"
    out.println "import lombok.EqualsAndHashCode;"
    out.println "import lombok.experimental.Builder;"
    out.println "import lombok.experimental.Accessors;"
    out.println "import org.hibernate.annotations.GenericGenerator;"
    out.println ""

    Set types = new HashSet()

    fields.each() {
        if (!ignoreColumns(it)) {
            types.add(it.type)
        }
    }

    if (types.contains("Instant")) {
        out.println "import java.time.Instant;"
    }

    if (types.contains("InputStream")) {
        out.println "import java.io.InputStream;"
    }

    /*add notes*/
    out.println ""
    out.println "/**\n" +
            " * @author  lh\n" +
            " * createTime: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " \n" +
            " */"
    out.println ""


    out.println "@Data"
    out.println "@Entity"
    out.println "@Schema(description=\"$tableComment\")"
    out.println "@SuperBuilder"
    out.println "@NoArgsConstructor"
    out.println "@AllArgsConstructor"
    out.println "@Accessors(chain = true)"
    out.println "@EqualsAndHashCode(callSuper = true)"
    out.println "@org.hibernate.annotations.Table(appliesTo = \"$tableName\", comment =\"$tableComment\")"
    out.println "public class $className  extends Audited {"

    fields.each() {
        if (!ignoreColumns(it)) {
            // increment judgment
            if ("id".equalsIgnoreCase(it.colum)) {
                out.println "\t"
                out.println "\t@Id"
                out.println "\t@GenericGenerator(name = \"sequence_id\", strategy = \"com.yunmo.id.HibernateIdentifierGenerator\")"
                out.println "\t@GeneratedValue(generator = \"sequence_id\")"
            }
            // Output Comment
            if (isNotEmpty(it.comment)) {
                out.println "\t/**"
                out.println "\t * ${it.comment.toString()}"
                out.println "\t */"
                out.println "\t@Schema(description = \"$it.comment\")"
            }

            if (it.annos != "") out.println "  ${it.annos}"
            if (isNotEmpty(it.comment)) {
                out.println "\t@Column(columnDefinition =  \"${it.columnDefinition} comment '${it.comment}'\") "
            } else {
                out.println "\t@Column(columnDefinition =  \"${it.columnDefinition}\") "
            }
            if (it.type == "EnumName") {
                out.println "\t@Enumerated(value = EnumType.STRING)"
            }
            out.println "\tprivate ${it.type} ${it.name};"
            out.println ""
        }
    }

//    out.println "  \tpublic static String getEntityNotFoundExceptionMessage(String columns, String values) {\n" +
//            "    \treturn String.format(\"%s:%s,对应${tableName}实体不存在\", columns, values);\n" +
//            "  \t}\n"
//    out.println "    public static String getEntityExistsExceptionMessage(String columns, String values) {\n" +
//            "    \treturn String.format(\"%s:%s,对应${tableName}实体已存在\", columns, values);\n" +
//            "  \t}\n"

    out.println "}"
}

private boolean ignoreColumns(it) {
    it.name in [ 'createBy', 'createTime', 'lastModifiedBy', 'lastModifiedTime', 'createdTime', 'createdBy']
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name            : javaName(col.getName(), false),
                           colum           : col.getName(),
                           type            : typeStr,
                           comment         : col.getComment(),
                           annos           : "",
                           columnDefinition: col.getDataType()
                   ]]
    }
}

def javaName(str, capitalize) {
    def s = str.split(/(?<=[^\p{IsLetter}])/).collect { Case.LOWER.apply(it).capitalize() }
            .join("").replaceAll(/[^\p{javaJavaIdentifierPart}]/, "_").replaceAll(/_/, "")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}


def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}

