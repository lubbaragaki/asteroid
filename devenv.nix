{
  pkgs,
  lib,
  config,
  ...
}:
{
  # https://devenv.sh/packages/
  packages = [
  	pkgs.scala-cli
  ];

  # https://devenv.sh/languages/
  languages = {
    java = {
      enable = true;
      jdk.package = pkgs.jdk11_headless;
    };
    scala.enable = true;
  };

  dotenv.disableHint = true;
  
  enterShell = ''
  	source .env
	astrun() {
		scala-cli run $SRC_DIR
	}
	astcomp() {
		scala-cli compile $SRC_DIR
	}
  '';

  # See full reference at https://devenv.sh/reference/options/
}

