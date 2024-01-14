import sys
import os
import face_recognition as fr
import numpy as np

"""
    Exit codes:
    0 - Exited Normally
    -1 - Internal Error
    1 - User Error
"""

arg: str = sys.argv[1]

# check if the arg from the server is null
if arg is None:
    exit(-1)

# change the working directory to the image folder for image processing
os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), '..', '..', '..', 'images'))
arg = os.path.join(os.path.dirname(os.path.abspath(__name__)), arg)

# check if the file exists
if not os.path.exists(arg):
    exit(-1)

faces: list = fr.face_encodings(fr.load_image_file(arg))

# check if there is no face or more than 1 face
if (len(faces) == 0) | (len(faces) >= 2):
    exit(1)

raw_folder: str = os.path.join(os.path.dirname(os.path.abspath(__name__)), '..', 'raw_data')

# check if the raw data folder exists
if not os.path.exists(raw_folder):
    os.makedirs(raw_folder)

# save to file for later processing and exit
np.save(file=os.path.join(raw_folder, os.path.basename(arg).split('.')[0]), arr=faces)
exit(0)
