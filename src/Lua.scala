package lua

//> using scala 3.3.6
//> using dep com.lihaoyi::os-lib:0.11.6
//> using dep org.virtuslab::scala-yaml:0.3.1
//> using dep org.luaj:luaj-jse:3.0.1
//> using dep com.lihaoyi::fansi:0.5.1

import os.*
import org.virtuslab.yaml.*
import fansi.Color
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

object Lua {
    def runScript(filename: String, output: String) = {
        val globals: Globals = JsePlatform.standardGlobals();
        val chunk: LuaValue = globals.loadfile(filename);
        // Invoke takes a Varargs as argument and returns a varargs
        val result: Varargs = chunk.invoke(LuaValue.valueOf(output));
        result.arg(1);
    }
}
