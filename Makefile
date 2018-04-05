GIT_ADDR=git@github.com:KyroNedyah/CS_6_005_Project1.git


PACKAGE=twitter

MAIN=Main
SOURCES=Extract.java Filter.java Main.java SocialNetwork.java Timespan.java Tweet.java TweetReader.java
TESTS=ExtractTest FilterTest SocialNetworkTest
TEST_SOURCES=$(foreach t,$(TESTS),$(t).java)
TEST_LOG=test_log.txt

SOURCE_PATH=src/$(PACKAGE)
TEST_PATH=test/$(PACKAGE)
LIB_PATH=lib
BIN_PATH=bin
LOG_PATH=log

SOURCE_LIST=$(foreach s,$(SOURCES),$(SOURCE_PATH)/$(s))
TEST_LIST=$(foreach t,$(TEST_SOURCES),$(TEST_PATH)/$(t))

LIBRARIES=javax.json-1.0.jar
LIB_LIST=$(foreach l,$(LIBRARIES),$(LIB_PATH)/$(l))


JAVA=java
JAVAC=javac
JAVAC_FLAGS=
CLASSPATH=/usr/share/java/junit.jar:/usr/share/java/junit4.jar:.:$(LIB_LIST)
JUNIT_CORE=org.junit.runner.JUnitCore
JUNIT_FLAGS=-ea

all:
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_PATH) $(JAVAC_FLAGS) $(SOURCE_LIST) $(TEST_LIST)

run: all
	(cd $(BIN_PATH) && $(JAVA) -cp $(CLASSPATH) $(PACKAGE).$(MAIN))

test: all $(TESTS)

$(TESTS): %:
	(cd $(BIN_PATH) && $(JAVA) -cp $(CLASSPATH) $(JUNIT_FLAGS) $(JUNIT_CORE) $(PACKAGE).$@ > ../$(LOG_PATH)/$@_$(TEST_LOG)); test $$? -le 1
	cat $(LOG_PATH)/$@_$(TEST_LOG) >> $(LOG_PATH)/$(TEST_LOG)


clean:
	rm -rf $(SOURCE_PATH)/*.class
	rm -rf $(TEST_PATH)/*.class
	rm -rf $(BIN_PATH)/$(PACKAGE)/*.class
	rm -rf $(LOG_PATH)/*



git_init:
	git init
	git remote add origin $(GIT_ADDR)

git_prep:
	(git add *); test $$? -le 1
	git status
	git diff

git_commit: git_prep
	git commit
	git log

git_push:
	git push -u origin master
