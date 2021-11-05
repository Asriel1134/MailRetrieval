# MailRetrieval
## InvertedIndex 倒排索引
`倒排记录格式：词项 (文件名1;出现次数;出现位置1|出现位置2|出现位置3),`
### 词项处理
⭐仅记录邮件正文内容

1.词条化：

&emsp;&emsp;使用正则表达式`\pP|\pS|\pC|\pN|\pZ`匹配并去除大部分符号，留下单词内容作为词项。

2.停用词：

&emsp;&emsp;暂未处理。

3.归一化：

&emsp;&emsp;暂未处理。
### 倒排记录
⭐使用的存储格式为哈希表：`HashMap(String, ArrayList<Object[]>);`

+ `key`为词项；
+ `value`为存储词项数据的`ArrayList<Object[]>`，每有一个文件中存在此词项便添加一个`Object[]`，
  + `Object[0]`为文件名
  + `Object[1]`为词项在文件中的出现次数
  + `Object[2]`为ArrayList<Integer>,存储词项在文件中的出现位置。
### 方法注释
+ 创建倒排索引：`public static void createInvertedIndex(String directory)`
  + 参数：`String directory`：要创建索引的目录。
  + 返回值： 无
+ 递归获取目录下文件名：`public static String[] getFileNames(String directory)`
  + 参数：`String directory`:目录。
  + 返回值：包含目录下所有文件名的数组。
+ 创建倒排记录表：`public static HashMap<String, ArrayList<Object[]>> createIndex(String[] fileNames)`
  + 参数：`String[] fileNames`：要创建倒排记录的文件名数组。
  + 返回值：倒排记录表。
+ 获取邮件正文词项：`public static String[] getText(String filePath)`
  + 参数：`String filePath`：文件路径。
  + 返回值：词项数组。
+ 检查词项数据中是否存在某文件：`public static int isContain(ArrayList<Object[]> list, String file)`
  + 参数：`ArrayList<Object[]> list`:词项数据。 `String file`:文件名。
  + 返回值：词项数据中是否存在此文件。
+ 将倒排记录写入文件：`public static void outputInvertedIndex(HashMap<String, ArrayList<Object[]>> invertedIndex)`
  + 参数：`HashMap<String, ArrayList<Object[]>> invertedIndex`：倒排记录表。
  + 返回值：无
+ 读取倒排记录文件：`public static HashMap<String, ArrayList<Object[]>> readInvertedIndex(String fileName)`
  + 参数：`String fileName`：倒排记录文件。
  + 返回值：倒排记录表。
***
## BooleanSearch 布尔检索
### 