## SCSMiner
This repository mines frequent SCS (Structured Call Sequence) items from Java code corpus.
### Setup
All static methods you need to modify is currently implemented in `src/utils/Config.java`.
1. Clone the repository, build a local maven project in your Java IDE.
2. To specify the code corpus, modify `getRepoCorpusPath` method with local path to your java files. We also provide `src/corpus/GithubCodeSearcher.java` to download online code, which will return top 100 Java files with specified query words. A Github access token is needed in `getGithubAccessToken` method.
3. Our frequent sub-sequence mining is based on existing PrefixSpan algorithm. Make sure you have installed the implementation from `https://github.com/chuanconggao/PrefixSpan-py` and you can run the command in terminal.