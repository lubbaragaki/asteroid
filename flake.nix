{
  description = "Asteroid - Scala CLI application";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      packages.${system} = {
        default = pkgs.stdenvNoCC.mkDerivation {
          pname = "asteroid";
          version = "0.1.0";
          src = builtins.fetchurl {
	       url = "https://github.com/lubbaragaki/asteroid/releases/download/1.0/asteroid.jar";
               sha256 = "8191f1ba950a5fba62216fc6695ba7e5d9f4b34a39cd83db70071b6ef7bc0413";
	    };
          dontUnpack=true;

          buildInputs = [ pkgs.makeWrapper pkgs.jre ];

          installPhase = ''
            runHook preInstall
            mkdir -p $out/{lib,bin}
            cp $src $out/lib/
            
            makeWrapper ${pkgs.jre}/bin/java $out/bin/asteroid \
              --add-flags "-jar $out/lib/*-asteroid.jar"
            runHook postInstall
          '';

          meta = with pkgs.lib; {
            description = "Asteroid Scala application";
            platforms = platforms.linux;
          };
        };
      };
    };
}

