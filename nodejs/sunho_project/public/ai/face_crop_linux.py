import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2' 
from googleapiclient import  discovery
from oauth2client.client  import GoogleCredentials
import sys
import io
import base64
from PIL import Image
from PIL import ImageDraw
from genericpath import isfile
from oauth2client.service_account import ServiceAccountCredentials
import tensorflow as tf
import matplotlib.pyplot as plt
from keras.models import load_model
import keras
import cv2
import numpy as np
import os
import math
import json

  # or any {'0', '1', '2'}
gpus = tf.config.experimental.list_physical_devices('GPU')
if gpus:    # Currently, memory growth needs to be the same across GPUs
    for gpu in gpus:
        tf.config.experimental.set_memory_growth(gpu, True)
        logical_gpus = tf.config.experimental.list_logical_devices('GPU')    
    # Memory growth must be set before GPUs have been initialized

NUM_THREADS = 10
MAX_RESULTS = 1
IMAGE_SIZE = 96,96
 
class FaceDetector():
    def __init__(self):
        # initialize library
        #credentials = GoogleCredentials.get_application_default()
        scopes = ['https://www.googleapis.com/auth/cloud-platform']
        credentials = ServiceAccountCredentials.from_json_keyfile_name(
                        'C:/sunho_project/public/ai/sunho-299311-f8d55ae6b161.json', scopes=scopes)
        self.service = discovery.build('vision', 'v1', credentials=credentials)
        #print ("Getting vision API client : %s" ,self.service)
 
    #def extract_face(selfself,image_file,output_file):
         
    def detect_face(self,image_file):
        try:
            with io.open(image_file,'rb') as fd:
                image = fd.read()
                batch_request = [{
                        'image':{
                            'content':base64.b64encode(image).decode('utf-8')
                            },
                        'features':[{
                            'type':'FACE_DETECTION',
                            'maxResults':MAX_RESULTS,
                            }]
                        }]
                fd.close()
         
            request = self.service.images().annotate(body={
                            'requests':batch_request, })
            response = request.execute()
            if 'faceAnnotations' not in response['responses'][0]:
                 print('[Error] %s: Cannot find face ' % image_file)
                 return None
                 
            face = response['responses'][0]['faceAnnotations']
            box = face[0]['fdBoundingPoly']['vertices']
            left = box[0]['x']
            top = box[1]['y']
                 
            right = box[2]['x']
            bottom = box[2]['y']
                 
            rect = [left,top,right,bottom]
                 
            print("[Info] %s: Find face from in position %s" % (image_file,rect))
            return rect
        except Exception as e:
            print('[Error] %s: cannot process file : %s' %(image_file,str(e)) )
             
    def rect_face(self,image_file,rect,outputfile):
        try:
            fd = io.open(image_file,'rb')
            image = Image.open(fd)
            draw = ImageDraw.Draw(image)
            draw.rectangle(rect,fill=None,outline="green")
            image.save(outputfile)
            fd.close()
            print('[Info] %s: Mark face with Rect %s and write it to file : %s' %(image_file,rect,outputfile) )
        except Exception as e:
            print('[Error] %s: Rect image writing error : %s' %(image_file,str(e)) )
         
    def crop_face(self,image_file,rect,outputfile):
        try:
            fd = io.open(image_file,'rb')
            image = Image.open(fd) 
            crop = image.crop(rect)
            im = crop.resize(IMAGE_SIZE,Image.ANTIALIAS)
            im.save(outputfile,"JPEG")
            fd.close()
            print('[Info] %s: Crop face %s and write it to file : %s' %(image_file,rect,outputfile) )
        except Exception as e:
            print('[Error] %s: Crop image writing error : %s' %(image_file,str(e)) )
         
    def getfiles(self,src_dir):
        files = []
        for f in os.listdir(src_dir):
            if isfile(os.path.join(src_dir,f)):
                if not f.startswith('.'):
                 files.append(os.path.join(src_dir,f))
 
        return files
     
    def rect_faces_dir(self,src_dir,des_dir):
        if not os.path.exists(des_dir):
            os.makedirs(des_dir)
             
        files = self.getfiles(src_dir)
        for f in files:
            des_file = os.path.join(des_dir,os.path.basename(f))
            rect = self.detect_face(f)
            if rect != None:
                self.rect_face(f, rect, des_file)
                 
    def crop_faces_dir(self,src_dir,des_dir):
         
        # training data will be written in $des_dir/training
        # validation data will be written in $des_dir/validate
         
        des_dir_training = os.path.join(des_dir,'training')
        des_dir_validate = os.path.join(des_dir,'validate')
         
        if not os.path.exists(des_dir):
            os.makedirs(des_dir)
        if not os.path.exists(des_dir_training):
            os.makedirs(des_dir_training)
        if not os.path.exists(des_dir_validate):
            os.makedirs(des_dir_validate)
         
        path,folder_name = os.path.split(src_dir)
        label = folder_name
         
        # create label file. it will contains file location
        # and label for each file
        training_file = open('training_file.txt','a')
        validate_file = open('validate_file.txt','a')
         
        files = self.getfiles(src_dir)
        cnt = 0
        for f in files:
            rect = self.detect_face(f)
 
            # replace ',' in file name to '.'
            # because ',' is used for deliminator of image file name and its label
            des_file_name = os.path.basename(f)
            des_file_name = des_file_name.replace(',','_')
             
            if rect != None:
                # 70% of file will be stored in training data directory
                if(cnt < 8):
                    des_file = os.path.join(des_dir_training,des_file_name)
                    self.crop_face(f, rect, des_file )
                    training_file.write("%s,%s\n"%(des_file,label) )
                # 30% of files will be stored in validation data directory
                else: # for validation data
                    des_file = os.path.join(des_dir_validate,des_file_name)
                    self.crop_face(f, rect, des_file)
                    validate_file.write("%s,%s\n"%(des_file,label) )
                     
                if(cnt>9):
                    cnt = 0
                cnt = cnt + 1
                 
        training_file.close()
        validate_file.close()
         
    def getdirs(self,dir):
        dirs = []
        for f in os.listdir(dir):
            f=os.path.join(dir,f)
            if os.path.isdir(f):
                if not f.startswith('.'):
                    dirs.append(f)
 
        return dirs
         
    def crop_faces_rootdir(self,src_dir,des_dir):
        # crop file from sub-directoris in src_dir
        dirs = self.getdirs(src_dir)
         
        #list sub directory
        for d in dirs:
            print('[INFO] : ### Starting cropping in directory %s ###'%d)
            self.crop_faces_dir(d, des_dir)
        #loop and run face crop
 
         
def main():
    srcdir= 'C:/sunho_project/public/images'
    desdir = 'C:/sunho_project/public/images'
    detector = FaceDetector()
 
    detector.crop_faces_rootdir(srcdir, desdir)
    
    model = load_model('C:/sunho_project/public/ai/fer2013plus_model.h5')
    gray = cv2.imread('C:/sunho_project/public/images/emo_pic.jpg',cv2.IMREAD_GRAYSCALE)
    img = cv2.resize(gray, (64,64))
    img = img.reshape((1,64,64,1))
    pred_y = model.predict(img)
    if pred_y[0][0]==max(pred_y[0]):
        json1='{"emotion":"0"}'
    elif pred_y[0][1]==max(pred_y[0]):
        json1='{"emotion":"1"}'
    elif pred_y[0][2]==max(pred_y[0]):
        json1='{"emotion":"2"}'
    elif pred_y[0][3]==max(pred_y[0]):
        json1='{"emotion":"3"}'
    elif pred_y[0][4]==max(pred_y[0]):
        json1='{"emotion":"4"}'
    elif pred_y[0][5]==max(pred_y[0]):
        json1='{"emotion":"5"}'
    elif pred_y[0][6]==max(pred_y[0]):
        json1='{"emotion":"6"}'
    elif pred_y[0][7]==max(pred_y[0]):
        json1='{"emotion":"7"}'
    
    b= json.loads(json1)

    print(json.dumps(b))

    #detector.crop_faces_dir(inputfile,outputfile)
    #rect = detector.detect_face(inputfile)
    #detector.rect_image(inputfile, rect, outputfile)
    #detector.crop_face(inputfile, rect, outputfile)
     
if __name__ == "__main__":
    main()