#version 330

uniform sampler2D colorbuffer;
/*uniform sampler2D noiseTex;
uniform sampler2D maskTex; */
uniform float deltatime; // seconds
uniform float luminanceThreshold = 0.2; // 0.2
uniform float colorAmplification = 4.0; // 4.0
//uniform float effectCoverage = 0.5; // 0.5
uniform float effectCoverage = 1; // 0.5

in  vec2 Texcoord;
out vec4 fragcolor;

void main ()
{
  vec4 finalColor;
  // Set effectCoverage to 1.0 for normal use.  
  if (Texcoord.x < effectCoverage)
  {
    vec2 uv;           
    uv.x = 0.4*sin(deltatime*50.0);
    uv.y = 0.4*cos(deltatime*50.0);
    //float m = texture2D(maskTex, gl_TexCoord[0].st).r;
    //vec3 n = texture2D(noiseTex, (gl_TexCoord[0].st*3.5) + uv).rgb;
    vec3 c = texture2D(colorbuffer, Texcoord.st).rgb;
  
    float lum = dot(vec3(0.30, 0.59, 0.11), c);
    if (lum < luminanceThreshold)
      c *= colorAmplification; 
  
    vec3 visionColor = vec3(0.1, 0.95, 0.2);
    finalColor.rgb = c * visionColor ;
   }
   else
   {
    finalColor = texture2D(colorbuffer,
                   Texcoord.st);
   }
  fragcolor.rgb = finalColor.rgb;
  fragcolor.a = 1.0;
}			