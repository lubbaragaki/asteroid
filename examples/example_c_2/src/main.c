// Takes a path string, creates a file in the specified path
// and outputs the path back without the path traversal
// example: given input "../../etc/something" prints "/etc/something"
// after creating the file
//
// Asteroid: should check that the path is well formed, ie.
// doesn't look like "//etc/something" and all path traversal
// attempts are removed, as well as check that the file is created
// only if the target is in the allowed directories
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

char* strip_path(char* path) {
    char* new_path = malloc(strlen(path) + 2);  // +2 for '/' and '\0'
    strcpy(new_path, "");  // Initialize safely
    for(int i=0; i<strlen(path); i++) {
        if(path[i] != '.' && path[i+1] != '.') {
            strncat(new_path, path+i, 1);
        }
    }
    return new_path;
}

char* rm_leading(char leading_char, char* string) {
    char* new_string = malloc(strlen(string)+1);
    if(string[0] == leading_char) {
	strncpy(new_string, string+1, strlen(string));
    } else {
	strcpy(new_string, string);
    }
    return new_string;
}

int main(int argc, char** argv) {
    char* path = "";
    // ((path = "%%";)){path}
    char* new_path = strip_path(path);
    char* final_path = rm_leading('/', new_path);
    printf("%s\n", final_path);
    return 0;
}

