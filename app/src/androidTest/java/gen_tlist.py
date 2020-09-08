import os
import subprocess
from functools import reduce
def to_class_name(class_path):
	
	return os.path.relpath(class_path , os.getcwd()).replace('\\','.').strip('.java')

black_list =[
	'com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest',
	'com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest',
	'com.example.examscanner.CustumTestSuite',
	'com.example.examscanner.persistence.remote.MsgSenderTest',
	'com.example.examscanner.StateFullTest',
	'com.example.examscanner.ViewModelsTestSuite',
	'com.example.examscanner.repositories.exam.ExamRepositoryTest'
]

def main():
	cmd = subprocess.Popen(["dir","*.java", "/S","/B"],stdout=subprocess.PIPE,shell=True)
	all_files = str(cmd.communicate()[0], 'utf-8')
	print(
		'{\n'+
		reduce(
			lambda acc,curr: acc+',\n'+curr+'.class',
			filter(
				lambda x: ('test' in x or 'Test' in x) and (not 'Abstract' in x) and not x in black_list,
				map(
					lambda x: to_class_name(x),
					all_files.split()
				)
			),
			""
		)
		+'\n}'
	)


if __name__ =='__main__':
	main()
