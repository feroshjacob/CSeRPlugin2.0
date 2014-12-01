package cser.utils;

public interface GlobalConfig {
	// we could load all the paths from a configuration file
	String seperator = "/"; //different for windows and Mac
	//String db_folder ="/Users//dhou/work/runtime-EclipseApplication/changes";
	String db_folder ="E:/runtime-EclipseApplication/changes";
    String CSeRFilesExt ="cser";
    double levenshteinRatio = 0.5; // measuring the DIS-similarity between two strings!!!

}
