{
  pkgs,
  lib,
  config,
  ...
}:
{
  # https://devenv.sh/packages/
  packages = [
    pkgs.lua
  ];

  # https://devenv.sh/languages/
  languages = {
    java = {
      enable = true;
      jdk.package = pkgs.jdk11_headless;
    };
    scala.enable = true;
  };

  # See full reference at https://devenv.sh/reference/options/
}

