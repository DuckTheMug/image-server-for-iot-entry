import sys
import os
import deepface.DeepFace as DeepFace

"""
    Exit codes:
    0 - Exited Normally
    -1 - Internal Error
    -2 - Input Error
"""

img: str = sys.argv[1]
db: str = sys.argv[2]

# check if the args from the server is null
if (img is None) | (db is None):
    exit(-1)

# change the working directory to the image folder for image processing
os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), 'images'))
root: str = os.path.dirname(os.path.abspath(__name__))
img = os.path.join(root, img)
db = os.path.join(root, db)

# check if the file or db exists
if (not os.path.exists(img)) | (not os.path.exists(db)):
    exit(-1)

if DeepFace.find(img_path=img, db_path=db).__len__() > 0:
    exit(0)
else:
    exit(-2)
