# Description
Asteroid is a simple, language-agnostic cli fuzz-testing tool for software. It uses templating and a yaml configuration file, allowing you to combine multiple variables and get varied results. Its purpose is helping you find ege cases in your code without going to the trouble of setting up a random test generating module yourself.

![demo](https://github.com/lubbaragaki/asteroid/tree/main/src/main/resources/demo.gif)

# Usage
Simply run the program without arguments, and it will automatically search upwards from the current directory until an `asteroid.belt` file is found or the home directory is encountered, at which point it will exit and show an error message.

## Templating
The templating system is very simple:
- Anything between double parentheses `(( anything ))` is treated as a template line and therefore only what is inside the double parentheses, after being processed, is written back to the line. Note that the indentationis relevant to soome programming languages, so leading spaces are important.
- Inside the double parentheses, the `%%` marker is replaced by the test value of the iteration
- After the double parentheses, `{name}` indicates the variable name, which is needed to give a specific set of test values to a specific variable
As an example, this `# ((num = %%)){num}` becomes this `num = value` where `value` is the test value of the current iteration.

## Configuration
The configuration is based on yaml syntax. The fields are:
- `build`: a list of commands to be executed in order to build the target program at each new iteration
- `run`: a list of commands to run the target program at each new iteration. Note that both build and run comands are executed sequentially, so you can leave either one of them with a single empty string element and the other one will execute the given commands
- `wordlists`: a mapping of variable names and wordlists. Asteroid looks for wordlists in ~/.local/share. The variable names must be the same as the one between curly braces in the template lines, and there shouldn't be more or less wordlists than there are variables
- `files`: a list of the files that contain template variables. This is to avoid looking through all the filetree of the project at each iteration, making it considerably faster
See [example](https://github.com/lubbaragaki/asteroid/blob/main/asteroid.belt)




