github使用
gitbash下载地址 https://gitforwindows.org/

步骤，注册github账号，建立一个仓库（ Create a New Repository），下载git bash
在git bash中输入

首先，获取通行证SSH Key
ssh-keygen -t rsa -C  "13144061737@163.com"    #github邮箱
成功的话会在~/下生成.ssh文件夹，进去，打开id_rsa.pub，复制里面的所有值。
登录github新增一条数据


测试是否成功连接
ssh -T git@github.com
出现 You've successfully authenticated, but GitHub does not provide shell access 。这就表示已成功连上github。

配置本客户端信息（他上传代码时候表明是该用户）
$ git config --global user.name "your name"
$ git config --global user.email "your_email@youremail.com"
查看当前用户名
git config --global user.name

克隆远程仓库
git clone git@github.com:GCUCST/test1.git

进入对应目录
cd repository1

创建文件并且提交说明并且推送
touch 55.md

git add .

git commit -m "说明"

git push origin master


配置对应的远程的仓库
git remote add origin git@github.com:GCUCST/test1.git
删除配置
git remote rm origin

建立本地仓库
git init

git add *

git commit -m "代码提交信息"

git push origin master
推送失败可能是远程仓库存在本地仓库没有的文件（重新拉一下一下就好）
git pull --rebase origin master

推送时候出现（出现不相关的分支）
fatal: refusing to merge unrelated histories
解决
git pull origin master --allow-unrelated-histories
git push --set-upstream origin master

git分支
git branch -b  分支                         #创建并切换分支
在master分支：git merge  分支              #合并分支
git branch -D 分支                          #删除本地分支
git push origin --delete 分支               #删除远程分支

查看系统配置
$ git config --system -l   #-l    表示 --list
查看全局配置
$ git config --global -l
其本质是存在与磁盘上的一个文件
例如global的文件位于
C:\Users\Administration\.gitconfig

切换到已经存在的分支
git checkout -b dev origin/dev

（拉取服务器代码时，能拉到所有分支，但是本地不显示，所以需要告诉git，切换的是已经有的分支）


git stash save "you message"
### 应用场景，当你代码写到一半，但需要切换到里一个分支,但又不想commit，可以使用该命令暂存
### 当你切换回该分支后，你可以使用 git stash pop 恢复到之前的进度
git stash show