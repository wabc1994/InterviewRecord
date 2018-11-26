# sed
sed(stream editor)流编辑器是一个很好的文件处理工具，本身是一个管道命令，主要是以行为单位进行处理，可以将数据行进行替换、删除、新增、选取等特定工作，下面先了解一下sed的用法


# 1. 查看命令
1. sed --help；查看具体使用规则：

2. sed -n 'xp' filename；显示文件X行命令：   

3. sed -n 'x,yp' filename；显示文件X行到Y行的内容：    

 
举例：

sed -n 4,8p file #打印file中的4-8行
sed -n 4p file   ## 打印file中的第四行文本