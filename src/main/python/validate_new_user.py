import sys
import os
import deepface.DeepFace as DeepFace

"""
    Exit codes:
    0 - Exited Normally
    -1 - Internal Error
    -2 - Input Error
"""

arg: str = sys.argv[1]

# check if the arg from the server is null
if arg is None:
    exit(-1)

# change the working directory to the image folder for image processing
os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), 'images'))
arg = os.path.join(os.path.dirname(os.path.abspath(__name__)), arg)

# check if the file exists
if not os.path.exists(arg):
    exit(-1)

face: int = DeepFace.extract_faces(arg).__len__()

# check if there is no face or more than 1 face
if (face == 0) | (face >= 2):
    exit(-2)

# clear cache if exists
cache: str = os.path.join(os.path.dirname(arg), "representations_vgg_face.pkl")
if os.path.exists(cache):
    os.remove(cache)

# using find itself to generate the cache
DeepFace.find(os.path.dirname(arg), arg)

exit(0)
