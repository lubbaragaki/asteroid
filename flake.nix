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
        default = pkgs.stdenv.mkDerivation {
          pname = "asteroid";
          version = "0.1.0";
          src = self;

          nativeBuildInputs = [ pkgs.scala-cli pkgs.makeWrapper ];
          buildInputs = [ pkgs.jre ];

          buildPhase = ''
            runHook preBuild
            scala-cli --power package src --assembly -o asteroid.jar -f
            runHook postBuild
          '';

          installPhase = ''
            runHook preInstall
            mkdir -p $out/{lib,bin}
            cp asteroid.jar $out/lib/
            
            makeWrapper ${pkgs.jre}/bin/java $out/bin/asteroid \
              --add-flags "-jar $out/lib/asteroid.jar"
            runHook postInstall
          '';

          meta = with pkgs.lib; {
            description = "Asteroid Scala application";
            license = licenses.unfree;  # Adjust as needed
            platforms = platforms.linux;
          };
        };
      };

      # For use in NixOS configuration
      overlays.default = final: prev: {
        asteroid = self.packages.${system}.default;
      };

      # Dev shell for working on the project
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          scala-cli
          jdk
        ];
      };
    };
}

