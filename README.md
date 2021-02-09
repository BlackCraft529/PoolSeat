## PoolSeat<br>
    基于HikariCP的一款连接池插件。
    较大程度上方便了开发者进行相关的数据库操作。

### 支持功能<br>
1.使用Java Bean更新相关数据库信息;<br>
2.可通过yml配置将数据库信息封装成对象并返回;<br>
3.支持通过文件来进行MySql相关操作(例如update,select等);<br>
4.使用SqlUtil工具对象,相关查询、修改、插入等操作仅需一行代码;<br>
5.支持自动读取数据库信息;<br>
6.连接池信息查询等;<br>
7.Json数据存取,String数据存取;<br>
8.序列化与反序列化对象为Json数据并更新;<br>
9.使用JavaBean来更新Json数据至数据库;<br>
10.实现完全通过配置进行数据库操作(推荐使用);<br>

### 相关配置信息<br>
* 当使用该插件作为前置时,请在plugin.yml中添加依赖: "depend: [PoolSeat]"<br>
* 您需要封装的类必须拥有一个无参构造器;<br>
* 您需要有一个所有字段和数据库字段所匹配的类(若字段名不匹配可以配置映射表)来进行相关的数据封装;<br>
* 您需要配置相关映射信息;<br>
* 使用插件前,您需要准备两个yml文件: <br>
  * 类映射文件,格式如下:<br>
    >类名:<br>
    >>path: 类的路径<br>
    >>table: 所对应的数据库表<br>
    >>reflectMap:<br>
    >>>\- 'column:数据库字段名<->bean:类字段名'<br>
    * 例如:<br>
    ```yaml
    GlobalPlayer:
      path: com.bc.pokerankpro.rankbattle.domain.player.GlobalPlayer
      table: player_data
      reflectMap:
        - 'column:id<->bean:name'
        - 'column:uuid<->bean:uuid'
        - 'column:rep_credit<->bean:repCredit'
        - 'column:cre_update_time<->bean:creUpdateDate'
        - 'column:season<->bean:season'
        - 'column:last_match<->bean:lastMatch'
        - 'column:cre_update_type<->bean:creUpdateType'
        - 'column:match_time<->bean:matchTime'
        - 'column:last_login<->bean:lastLoginDate'
        - 'column:match_data<->bean:matchData'
    ```
  * 方法文件,格式如下
  >操作名(自取,唯一):<br>
  >>cmd: "数据库语句,如select * from player where uuid=?;"<br>
  >>return: 返回类型,类地址,String与Player类型特殊例外可直接写String和Player<br>
  >>parameters:<br>
  >>>\- '参数,对齐语句中的?号,此处对应uuid后的?号'<br>
  * 如果想要将某个字段转换成Bukkit的Player/OfflinePlayer:<br>
  >操作名(自取,唯一):<br>
  >>cmd: "数据库语句,如select * from player where uuid=?;"<br>
  >>column: "数据库中用于转换的字段名,例如uuid"<br>
  >>type: "UUID/NAME(是名称转换或者是UUID转换)"<br>
  >>return: "Player"(此处必填Player,否则无法正常获取)<br>
  >>parameters:<br>
  >>>\- '参数,对齐语句中的?号,此处对应uuid后的?号'<br>
  * 如果想使用JavaBean更新数据库:<br>
  >updateGlobalPlayer:<br>
  >>#此处变量对应为 数据库字段名 ,非 类字段名<br>
  >>cmd: "update player_data set season=\<season> where uuid=\<uuid>;"<br>
    * 例如:<br>
    ```yaml
    selectAllGlobalPlayer:
      cmd: "select * from player_data;"
      parameters: []
      return: com.bc.pokerankpro.rankbattle.domain.player.GlobalPlayer
    #查询玩家并封装
    selectPlayer:
      #指令集
      cmd: "select * from player_data;"
      #字段名
      column: "uuid"
      #类型： UUID / NAME
      type: UUID
      #参数集
      parameters:
        - ''
      #该类型可直接写 Player（返回为：Player或OfflinePlayer）
      return: Player
    updateGlobalPlayer:
      #此处变量使用 <数据库字段>
      cmd: "update player_data set season=<season> where uuid=<uuid>;"
    selectStringData:
      #指令集
      cmd: "select * from player_data;"
      #字段名
      column: "uuid"
      parameters: []
      #String类型可以为“String”或“java.lang.String”
      return: "java.lang.String"
    #使用yml更新json数据，会将整个javaBean转换为json数据，存入相应的字段中;
    #想要使用当前带入的bean中的某个字段作为条件，变量使用 <数据库字段>;
    updateJsonData:
      #带入类的json变量为 <#object_json_String#>
      cmd: "update player_data set json_data = <#object_json_String#> where uuid = <uuid>;"
    ```
  * 注意:如果你希望将JavaBean转换为指定方法,请在语句中使用\<#object_json_String#>来替换将要被写入的json数据<br>
  * 例如:update player_data set json_data = \<#object_json_String#> where uuid = \<uuid>;<br>
  * 则是将您在代码中带入的Object转换为Json数据后存入与Object中对应的uuid字段的记录中;<br>
  * 同时: 您在更新语句中的所有bean字段变量均使用数据库中所对应的字段进行填写!!即映射表中的column!!!<br>
### 初始化工具类<br>
* 为了让数据库成功连接,请您在任意一yml中进行如下配置:<br>
```yaml
MySql:
  DatabaseName: databasename
  UserName: root
  Password: 123456
  Port: 3306
  Ip: localhost
  #连接池池容量
  PoolSize: 10
  ConnectParameter: "useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"
```
* 随后在合适的位置键入如下代码:<br>
```java
//ConfigUtil.getConfig() 获取为上述文件的 FileConfiguration 对象
//plugin为当前插件
SqlUtil sqlUtil = new SqlUtil(plugin,ConfigUtil.getConfig());
```
* 或者您可以使用SqlConfig对象来进行初始化:<br>
```java
//public SqlConfig(String databaseName, String userName, String password, String ip, String port, int poolSize, String connectParameter);
SqlUtil sqlUtil = new SqlUtil(sqlConfig);
```
* 至此,工具初始化完毕.<br>

### 进行生♂产<br>
* 需要注意的一点是: 如果您想查询某个数据并获取封装类返回,默认的返回值是List<Object>;<br>
* 那么如何进行查询呢?<br>
  * 1.使用yml:<br>
  ```java
  sqlUtil.selectData(SqlConfigUtil.getSqlConfig(),"selectAllGlobalPlayer");
  //如何使用获取的数据呢?一般来说进行遍历,或者直接get数据即可;
  for (Object globalPlayer: sqlUtil.selectData(SqlConfigUtil.getSqlConfig(),"selectAllGlobalPlayer")){
    System.out.println(globalPlayer.toString());
  }
  ```
  * 2.使用语句并带入参数:<br>
  ```java
    //待更新本部分教程代码
  ```
* 如何更新数据库数据?<br>
  * 1.使用yml:<br>
  ```java
  //获取实体后修改相关数据,实体为 GlobalPlayer 类型的 newPlayer , 如下方法将返回受影响条数
  sqlUtil.updateDataFromBean(SqlConfigUtil.getSqlConfig(),newPlayer,"updateGlobalPlayer");
  ```
  * 2.使用语句并带入参数:<br>
  ```java
  //待更新本部分教程代码
  ```
* 其他: 更多功能教程待更新...<br>
